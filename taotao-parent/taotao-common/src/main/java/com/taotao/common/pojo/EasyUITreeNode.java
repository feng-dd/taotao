package com.taotao.common.pojo;

import java.io.Serializable;

/**
 * EasyUI树节点pojo  tb_item_cat
 */
public class EasyUITreeNode implements Serializable {

	/**
	 * id
	 */
	private long id;
	/**
	 * name
	 */
	private String text;
	/**
	 * is_parent
	 */
	private String state;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
