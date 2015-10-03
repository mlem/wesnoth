package org.wesnoth.gateway.replays;

import org.junit.Test;
import org.wesnoth.ReplayMeta;
import org.wesnoth.connection.ExternalServiceException;
import org.wesnoth.connection.replays.ReplayConnection;
import org.wesnoth.usecase.replay.ReplayLoader;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReplayGatewayImplTest {

    @Test
    public void testCanFindAllReplaysForVersion1Point12And20150921() throws ExternalServiceException {
        ReplayGateway replayGateway = new ReplayGatewayImpl();

        ReplayConnection replayConnection = mock(ReplayConnection.class);
        when(replayConnection.connect()).thenReturn(getClass().getResourceAsStream("replays_1.12_20150921.html"));
        when(replayConnection.currentUrl()).thenReturn("replays.wesnoth.org/1.12/20150921/");

        List<ReplayMeta> replays = replayGateway.listReplays(replayConnection);

        assertThat(replays.size(), is(559));
    }

    @Test
    public void testReplayCanLoadMetaData() throws ExternalServiceException {
        ReplayGateway replayGateway = new ReplayGatewayImpl();

        ReplayConnection replayConnection = mock(ReplayConnection.class);
        when(replayConnection.connect()).thenReturn(getClass().getResourceAsStream("2p__Silverhead_Crossing_Turn_1_(975).bz2"));
        when(replayConnection.currentUrl()).thenReturn("replays.wesnoth.org/1.12/20150921/2p__Silverhead_Crossing_Turn_1_(975).bz2");

        Replay replay = replayGateway.loadReplay(new ReplayLoader(), replayConnection);
        assertThat(replay.getCampaignType(), is(Replay.CampaignType.MULTIPLAYER));
        assertThat(replay.getDifficulty(), is(Replay.Difficulty.NORMAL));
        assertThat(replay.getLabel(), is("2p — Silverhead Crossing Turn 1"));
        assertThat(replay.getGameTitle(), is("ps Partie"));
        assertThat(replay.getRandomSeed(), is(11232));
        assertThat(replay.getVersion(), is("1.12.0"));
    }
}