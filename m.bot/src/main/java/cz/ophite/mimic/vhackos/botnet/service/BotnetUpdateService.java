package cz.ophite.mimic.vhackos.botnet.service;

import com.google.gson.Gson;
import cz.ophite.mimic.vhackos.botnet.Application;
import cz.ophite.mimic.vhackos.botnet.Botnet;
import cz.ophite.mimic.vhackos.botnet.api.IBotnet;
import cz.ophite.mimic.vhackos.botnet.dto.BotnetUpdateData;
import cz.ophite.mimic.vhackos.botnet.service.base.EndpointService;
import cz.ophite.mimic.vhackos.botnet.service.base.IService;
import cz.ophite.mimic.vhackos.botnet.service.base.Service;
import cz.ophite.mimic.vhackos.botnet.shared.command.CommandRunner;
import cz.ophite.mimic.vhackos.botnet.shared.injection.Inject;
import cz.ophite.mimic.vhackos.botnet.shared.utils.SharedUtils;
import cz.ophite.mimic.vhackos.botnet.utils.AudioPlayer;
import cz.ophite.mimic.vhackos.botnet.utils.ResourceHelper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * Služba pro aktualizaci bota.
 *
 * @author mimic
 */
@Inject
@EndpointService(IService.SERVICE_BOTNET_UPDATE)
public final class BotnetUpdateService extends Service {

    private static final String UPDATE_URL = "https://raw.githubusercontent.com/OphiteCorp/vhackos-botnet/master/changelog.chl";
    private static final long DEFAULT_TIMEOUT = 3600000; // 1h

    protected BotnetUpdateService(Botnet botnet) {
        super(botnet);
    }

    @Override
    public String getDescription() {
        return "Checks for Botnet updates";
    }

    @Override
    protected void initialize() {
        setTimeout(DEFAULT_TIMEOUT);
    }

    @Override
    protected void execute() {
        try (var in = new BufferedReader(new InputStreamReader(new URL(UPDATE_URL).openStream(), "UTF-8"))) {
            var data = new Gson().fromJson(in, BotnetUpdateData.class);
            data.setNewVersionAvailable(!data.getVersion().equalsIgnoreCase(IBotnet.VERSION));
            reformatNews(data);
            getShared().setUpdateData(data);

            if (data.isNewVersionAvailable()) {
                getLog().warn("Attention! A new version v{} of Botnet is available. To download, go to this link: {}", data
                        .getVersion(), data.getDownloadLink());

                var result = CommandRunner.getInstance().run("latest", null);
                if (Application.isConsoleMode()) {
                    System.out.println(result);
                } else {
                    getLog().info("\n" + result + "\n");
                }
                AudioPlayer.play(ResourceHelper.getStream(ResourceHelper.ResourceValue.SOUND_NEW_VERSION));
            } else {
                getLog().info("Your Botnet version is up to date ;-)");
            }
            getLog().info("Information about the latest version of Botnet was obtained. Next check will be in: {}", SharedUtils
                    .toTimeFormat(DEFAULT_TIMEOUT));

        } catch (Exception e) {
            getLog().error("An error occurred while searching for the latest version of Botnet. Next check will be in: {}", SharedUtils
                    .toTimeFormat(DEFAULT_TIMEOUT));
        }
    }

    private static void reformatNews(BotnetUpdateData data) {
        if (data.getNews() == null || data.getNews().isEmpty()) {
            return;
        }
        var list = new ArrayList<String>(data.getNews().size());

        for (var n : data.getNews()) {
            list.add(SharedUtils.addLinebreaks(n, 75, "\n"));
        }
        data.setNews(list);
    }
}
