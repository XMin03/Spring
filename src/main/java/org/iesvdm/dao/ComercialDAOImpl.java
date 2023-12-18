package org.iesvdm.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Comercial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//Anotación lombok para logging (traza) de la aplicación
@Slf4j
@Repository
//Utilizo lombok para generar el constructor
@AllArgsConstructor
public class ComercialDAOImpl implements ComercialDAO {

	//JdbcTemplate se inyecta por el constructor de la clase automáticamente
	//
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void create(Comercial cliente) {
		// TODO Auto-generated method stub
		KeyHolder k=new GeneratedKeyHolder();
		String sqlInsert = """
							INSERT INTO comercial (nombre, apellido1, apellido2, comisión) 
							VALUES  (     ?,         ?,         ?,       ?)
									""";
		int rows = jdbcTemplate.update(con -> {
			PreparedStatement ps = con.prepareStatement(sqlInsert, new String[] { "id" });
			int idx = 1;
			ps.setString(idx++, cliente.getNombre());
			ps.setString(idx++, cliente.getApellido1());
			ps.setString(idx++, cliente.getApellido2());
			ps.setFloat(idx, cliente.getComision());
			return ps;
		},k);
		cliente.setId(k.getKey().intValue());
		log.info("Insertados {} registros.", rows);
	}

	@Override
	public List<Comercial> getAll() {
		
		List<Comercial> listComercial = jdbcTemplate.query(
                "SELECT * FROM comercial",
                (rs, rowNum) -> new Comercial(rs.getInt("id"), 
                							  rs.getString("nombre"), 
                							  rs.getString("apellido1"),
                							  rs.getString("apellido2"), 
                							  rs.getFloat("comisión"))
                						 	
        );
		
		log.info("Devueltos {} registros.", listComercial.size());
		
        return listComercial;
	}

	@Override
	public Optional<Comercial> find(int id) {
		// TODO Auto-generated method stub
		Comercial c=jdbcTemplate.queryForObject("SELECT * FROM comercial where id=?"
				, (rs, rowNum) -> new Comercial(rs.getInt("id"),
						rs.getString("nombre"),
						rs.getString("apellido1"),
						rs.getString("apellido2"),
						rs.getFloat("comisión"))
				,id);
		return (c==null)?Optional.empty():Optional.of(c);
	}

	@Override
	public void update(Comercial cliente) {
		// TODO Auto-generated method stub
		int rows=jdbcTemplate.update("UPDATE comercial set nombre=?, apellido1=?,apellido2=?,comisión=? where id=?",
				cliente.getNombre(),
				cliente.getApellido1(),
				cliente.getApellido2(),
				cliente.getComision(),
				cliente.getId());
		log.info("Update de Cliente con {} registros actualizados.", rows);
	}

	@Override
	public void delete(long id) {
		// TODO Auto-generated method stub
		int rows=jdbcTemplate.update("DELETE from comercial where id=?",id);
		log.info("Delete de Cliente con {} registros eliminados.", rows);
	}

}
