package kr.casealot.shop.domain.function.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kr.casealot.shop.global.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FUNCTION")
public class Function extends BaseTimeEntity {
  @JsonIgnore
  @Id
  @Column(name = "FUNCTION_ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private int newOrder;
  private int cancelManage;
  private int cancelRequest;
  private int returnManage;
  private int changManage;
  private int readyAnswer;
}
