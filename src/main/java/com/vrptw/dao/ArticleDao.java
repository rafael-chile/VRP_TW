package com.vrptw.dao;

import com.vrptw.entities.Article;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class ArticleDao extends GenericDao{


    @SuppressWarnings(value = "unchecked")
    public List<Article> getList(String idOrder) throws SQLException {
        String sqlQuery = "SELECT EHA.documento_final, " +
                "A.idArtigo, A.description_artigo, A.quantity, "+
                "E.qtd_pedida, E.unit_measure, E.encomenda "+
                "FROM artigos AS A "+
                "INNER JOIN encomendas_has_artigos as EHA "+
                "   ON A.idArtigo = EHA.artigo "+
                "INNER JOIN encomendas as E "+
                "   ON EHA.documento_final = E.idDocumento_final "+
                "WHERE E.encomenda = '"+ idOrder +"';";
        List<Article> dataList = null;
        try {
            dataList = this.read(Article.class, sqlQuery );
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        //dataList.stream().forEach(System.out::println);
        return dataList;
    }


}
