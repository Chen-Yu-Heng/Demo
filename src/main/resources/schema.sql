DROP TABLE IF EXISTS TB_COINDESK;

CREATE TABLE TB_COINDESK (
  CURCD VARCHAR(10) NOT NULL COMMENT '幣別',
  NAME VARCHAR(3) NOT NULL COMMENT '幣別中文名稱',
  RATE VARCHAR(3) NOT NULL COMMENT '匯率',
  CREATEDATE DATE NOT NULL COMMENT '建立時間',
  LASTUPDATEDATE DATE NOT NULL COMMENT '最後更新時間',
  PRIMARY KEY (CURCD) 
) ;