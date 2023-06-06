package kr.casealot.shop.domain.file.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import kr.casealot.shop.global.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploadService {
    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${upload.path}")
    private String uploadPath;

    public String uploadFile(MultipartFile multipartFile)
            throws Exception {
        // S3 버킷에 저장할 파일 주소 생성
        File file = convertMultiPartToFile(multipartFile)
                .orElseThrow(() -> new Exception("Converting Uploaded File is Invalid"));
        String fileName = uploadPath + "/" + FileUtil.generateSavedFileName(multipartFile.getOriginalFilename());
        // S3 버킷에 파일 저장
        String fileUrl = uploadFileToBucket(fileName, file);

        // 로컬에 저장한 파일 삭제
        removeNewFile(file);

        return fileUrl;
    }

    /**
     * S3 버킷에 저장된 파일을 삭제하는 함수.
     *
     * @param fileUrl S3 버킷 내에 파일이 저장된 URL.
     * @return 성공 메시지
     */
    public String deleteFileFromS3Bucket(String fileUrl) {

        // 삭제할 파일 이름
        String fileName = uploadPath + "/" + fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

        // 파일 삭제
        amazonS3Client.deleteObject(bucketName, fileName);
        return "Successfully deleted";
    }

    /**
     * 로컬에 저장된 파일을 삭제하는 함수
     *
     * @param targetFile
     */
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }

    /**
     * S3 버킷에 파일을 저장하는 함수.
     *
     * @param fileName 저장할 파일 이름
     * @param file     저장할 파일
     * @return S3 버킷에 저장한 파일 경로
     */
    private String uploadFileToBucket(String fileName, File file) {
        try {
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
            return amazonS3Client.getUrl(bucketName, fileName).toString();
        } catch (Exception e) {
            removeNewFile(file);
            // TODO 나중에 지워야함
            log.error("{}",e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
        }
    }

    /**
     * {@code <code>MultipartFile</code> 객체를 <code>File</code> 객체로 변환하는 함수.}
     *
     * @param file 변환할 파일
     * @return 변환된 {@code <code>File</code> 객체}
     * @throws IOException 변환에 실패할 경우
     */
    private Optional<File> convertMultiPartToFile(MultipartFile file)
            throws IOException {

        // 파일 생성
        File convertFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convertFile);

        // 파일 저장
        fos.write(file.getBytes());
        fos.close();
        return Optional.of(convertFile);
    }

}
