package com.jovan.erp_v1.save_as;

import java.util.Map;

/**
 *Genericki interfejs sa cuvanje n podataka.
 *T je entitet, R je DTO/response.
 *Parametar overrides je kao Python-ov kwargs — prosledjujem samo ono sto zelim da se prepise.
 */
public interface SaveAsService<T, R> {
    R saveAs(Long sourceId, Map<String, Object> overrides);
}