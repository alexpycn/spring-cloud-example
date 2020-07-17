package cn.alexpy.common.base;

import lombok.Data;

/**
 * @author alexpy
 */
@Data
public class BasePageDTO {

	/**
	 * 第几页 默认: 1
	 */
	private int page = 1;

	/**
	 * 每页数量 默认: 10
	 */
	private int rows = 10;

}
