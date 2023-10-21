package com.example.demo.repository.jbdi.mappers

import com.example.demo.domain.Board
import com.example.demo.domain.fromString
import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
import java.sql.SQLException

class BoardMapper : ColumnMapper<Board> {
    @Throws(SQLException::class)
    override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext?): Board {
        return fromString(r.getString(columnNumber))
    }
}