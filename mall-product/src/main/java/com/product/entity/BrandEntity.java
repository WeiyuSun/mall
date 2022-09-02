package com.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import com.common.validator.constraints.LimitedValue;
import com.common.validator.group.AddGroup;
import com.common.validator.group.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
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
	@NotNull(message = "Brand id missing", groups = {UpdateGroup.class})
	@Null(message = "Do not accept brand id from outside", groups = {AddGroup.class})
	private Long brandId;
	/**
	 * brand name
	 */
	@NotBlank(message = "Brand name cannot be empty", groups = {AddGroup.class})
	private String name;

	/**
	 * logo address in oss
	 */
	@NotEmpty(message = "logo address cannot be empty", groups = {AddGroup.class})
	@URL(message = "Brand logo address must be an valid url", groups = {AddGroup.class, UpdateGroup.class})
	private String logo;

	/**
	 * brand description
	 */
	private String descript;

	/**
	 * show status, 1 -> active. 0 -> inactive
	 */
	@NotNull(groups = {AddGroup.class}, message = "status value missing")
	@LimitedValue(values = {0, 1}, message = "status value can only be 0 or 1", groups ={AddGroup.class, UpdateGroup.class})
	private Integer showStatus;

	/**
	 * the search index
	 */
	@NotNull(groups = {AddGroup.class}, message = "search index missing")
	@Length(min = 1, max = 1, message = "first letter is one character from a-z or A-Z", groups = {AddGroup.class, UpdateGroup.class})
	@Pattern(regexp = "^[a-zA-Z]$", message = "first letter is one character from a-z or A-Z",groups = {AddGroup.class, UpdateGroup.class})
	//@Pattern(regexp ="^[a-zA-Z{1}]$", message = "first letter is one character from a-z or A-Z", groups = {AddGroup.class, UpdateGroup.class})
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull(groups = {AddGroup.class}, message = "Sort value missing")
	@Max(value = 100, message = "Sort value must an integer between 0 and 100", groups = {AddGroup.class, UpdateGroup.class})
	@Min(value = 0, message = "Sort value must an integer between 0 and 100", groups = {AddGroup.class, UpdateGroup.class})
	private Integer sort;

}
