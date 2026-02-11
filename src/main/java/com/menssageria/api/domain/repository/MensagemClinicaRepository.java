package com.menssageria.api.domain.repository;

import com.menssageria.api.domain.model.MensagemClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface MensagemClinicaRepository extends JpaRepository<MensagemClinica, Long> {

	@Modifying
    @Transactional
    @Query("UPDATE MensagemClinica m SET m.confirmado = true, m.dataLeitura = CURRENT_TIMESTAMP WHERE m.hash = :hash OR m.hash LIKE CONCAT('%', :hash, '%')")
    int confirmarPorHash(@Param("hash") String hash);

    @Modifying
    @Transactional
    @Query("UPDATE MensagemClinica m SET m.dataLeitura = CURRENT_TIMESTAMP WHERE m.zapiMessageId = :zapiId")
    void marcarComoConfirmada(@Param("zapiId") String zapiId);
    // Métodos para o Dashboard e Listagens
    List<MensagemClinica> findByConfirmado(Boolean status);
    List<MensagemClinica> findAllByOrderByDataEnvioDesc();
}