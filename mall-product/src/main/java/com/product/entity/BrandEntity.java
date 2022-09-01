package com.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.mybatis.spring.annotation.MapperScan;

import javax.validation.constraints.*;

/**
 * 品牌
 * 
 * @author weiyu
 * @email weiyu0203@qq.com
 * @date 2022-08-24 23:44:23
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * brand id
	 */
	@TableId
	private Long brandId;
	/**
	 * brand name
	 */
	@NotBlank(message = "Brand name cannot be empty")
	private String name;

	/**
	 * logo address in oss
	 */
	@NotEmpty
	@URL(message = "Brand logo address must be an valid url")
	private String logo;

	/**
	 * brand description
	 */
	private String descript;

	/**
	 * show status, 1 -> active. 0 -> inactive
	 */
	@NotNull
	@Min(value = 0, message = "status value can only be 0 or 1")
	@Max(value = 1, message = "status value can only be 0 or 1")
	private Integer showStatus;

	/**
	 * the search index
	 */
	@Pattern(regexp ="/^[a-zA-Z]{1}$/", message = "first letter is one character from a-z or A-Z")
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull
	@Max(value = 100, message = "Sort value must an integer between 0 and 100")
	@Min(value = 0, message = "Sort value must an integer between 0 and 100")
	private Integer sort;

}
