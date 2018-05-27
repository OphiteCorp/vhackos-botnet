package cz.ophite.mimic.vhackos.botnet.api.module.base;

import cz.ophite.mimic.vhackos.botnet.api.IBotnet;

/**
 * Rozhraní modulu.
 *
 * @author mimic
 */
public interface IModule {

    /**
     * Získá instanci botnetu.
     */
    IBotnet getBotnet();
}
