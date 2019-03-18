package com.taotao.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * EasyUI数据列表pojo
 */
public class EasyUIDataGridResult implements Serializable {

	/**
	 * 数据总条数
	 */
	private long total;
	/**
	 * 对象信息
	 */
	private List rows;

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}
}
