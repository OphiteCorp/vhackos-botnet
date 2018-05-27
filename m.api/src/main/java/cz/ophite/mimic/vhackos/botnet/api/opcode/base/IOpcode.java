package cz.ophite.mimic.vhackos.botnet.api.opcode.base;

import java.util.Map;

/**
 * Základní rozhraní pro opcode.
 *
 * @author mimic
 */
public interface IOpcode {

    /**
     * Získá cíl, na který opcode směřuje.
     */
    OpcodeTargetType getTarget();

    /**
     * Získá parametry opcode.
     */
    Map<String, String> getParams();

    /**
     * Získá hodnota opcode. Né všechny opcode mají kód.
     */
    default String getOpcodeValue() {
        return null;
    }

    /**
     * Pokud opcode bude samostatný, tak se nepridaji zadné dodatečné parametry jako třeba token, uid, jazyk apod.
     */
    default boolean isStandaloneOpcode() {
        return false;
    }
}

