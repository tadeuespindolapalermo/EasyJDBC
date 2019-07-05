package com.github.tadeuespindolapalermo.dao;

import com.github.tadeuespindolapalermo.persistence.PersistenceRepository;

public class Persistence implements PersistenceRepository {	

	@Override
	public void save(Object object, String table, String[] columns) {
		mountSQLInsert(table, columns);
	}

	private String mountSQLInsert(String table, String[] columns) {

		StringBuilder columnsName = new StringBuilder();
		StringBuilder values = new StringBuilder();
		
		int qtdColumns = columns.length;

		for (int i = 0; qtdColumns - 1 >= i; i++) {
			if (i != qtdColumns - 1) {
				columnsName.append(columns[i]).append(", ");
				values.append("?").append(", ");
			} else {
				columnsName.append(columns[i]);
				values.append("?");
			}
		}

		StringBuilder sql = new StringBuilder("INSERT INTO ")		
		.append(table)
		.append(" (" + columnsName.toString() + ") ")
		.append("VALUES")
		.append(" (" + values.toString() + ") ");

		return sql.toString();
	}

}
