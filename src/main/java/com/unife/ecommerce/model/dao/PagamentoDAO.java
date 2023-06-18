package com.unife.ecommerce.model.dao;

import com.unife.ecommerce.model.mo.Pagamento;
import com.unife.ecommerce.model.mo.TipoPagamento;
import java.sql.Date;

public interface PagamentoDAO {

    //Inserimento di un nuovo pagamento completo di dati relativi alla carta di credito/debito
    public Pagamento createFull(Date data, double totale, String numCarta, String mese, String anno, String cvv, TipoPagamento tipo, boolean deleted);

    //Inserimento di un nuvo pagamento (senza la informazioni carta) serve per: PayPal, bonifico e contrassegno
    public Pagamento createSimple(Date data, double totale,TipoPagamento tipo, boolean deleted);
}
