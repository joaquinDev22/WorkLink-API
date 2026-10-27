package com.example.Proyecto_Final_Progra3.util;

import java.security.SecureRandom;
import java.util.UUID;

public final class UuidV7Generator {

    private static final SecureRandom RANDOM = new SecureRandom();

    private UuidV7Generator() {}

    /**
     * Genera un UUID versión 7 conforme al estándar RFC 9562.
     * Incorpora timestamp de 48 bits en milisegundos para ordenamiento temporal
     * secuencial en índices B-Tree de bases de datos relacionales, más 74 bits
     * de entropía criptográfica.
     */
    public static UUID generate() {
        long timestamp = System.currentTimeMillis();

        // 48 bits de timestamp Unix en milisegundos
        long mostSigBits = (timestamp & 0xFFFFFFFFFFFFL) << 16;
        
        // 4 bits de versión (0111 = 7)
        mostSigBits |= 0x7000L;
        
        // 12 bits de entropía aleatoria (rand_a)
        mostSigBits |= (RANDOM.nextLong() & 0x0FFFL);

        // 2 bits de variante RFC (10)
        long leastSigBits = 0x8000000000000000L;
        
        // 62 bits de entropía aleatoria (rand_b)
        leastSigBits |= (RANDOM.nextLong() & 0x3FFFFFFFFFFFFFFFL);

        return new UUID(mostSigBits, leastSigBits);
    }
}

