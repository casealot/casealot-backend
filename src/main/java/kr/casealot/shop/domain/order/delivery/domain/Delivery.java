package kr.casealot.shop.domain.order.delivery.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "DELIVERY")
@Builder
public class Delivery {
  @JsonIgnore
  @Id
  @Column(name = "DELIVERY_ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "t_code")
  private String tCode;

  @Column(name = "t_invoice")
  private String tInvoice;

  @Column(name = "t_key")
  private String tKey;
}
