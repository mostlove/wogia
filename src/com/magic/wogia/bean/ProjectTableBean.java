package com.magic.wogia.bean;

import java.io.Serializable;

/**
 *  远程数据库 项目表 实体
 * @author QimouXie
 *
 */
public class ProjectTableBean implements Serializable {

	private static final long serialVersionUID = -2717334607754704142L;
	
	/**表名*/
	private String tableName;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((tableName == null) ? 0 : tableName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjectTableBean other = (ProjectTableBean) obj;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		return true;
	}

	

	
}
