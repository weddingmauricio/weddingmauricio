package com.wp.mconto.repository;

import com.wp.mconto.model.pago.Transaccion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransaccionRepo extends CrudRepository<Transaccion, Long> {

}
