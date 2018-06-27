package com.huobi.quantification.dto;

import java.io.Serializable;
import java.util.Date;
public class HuobiOrderSub implements Serializable {

  private Long id;

   private String symbol;


   private Long accountId;

	private String amount;

	private String price;

    private Date createdAt;

	private String type;

   private String fieldAmount;

   private String fieldfees;
   private Long userId;
   
   private String source;

   private String state;
   
   private String canceledAt;
   
   private String exchange;
   
   private String batch;
}
