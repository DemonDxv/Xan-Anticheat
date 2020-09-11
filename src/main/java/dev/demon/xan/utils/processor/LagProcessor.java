package dev.demon.xan.utils.processor;

import dev.demon.xan.api.tinyprotocol.api.Packet;
import dev.demon.xan.api.tinyprotocol.api.TinyProtocolHandler;
import dev.demon.xan.api.tinyprotocol.packet.in.WrappedInTransactionPacket;
import dev.demon.xan.api.tinyprotocol.packet.out.WrappedOutTransaction;
import dev.demon.xan.api.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * Created on 05/01/2020 Package me.jumba.sparky.util.processor
 */
@Getter
@Setter
public class LagProcessor {
    private User user;

    private long lastTransactionDiffPing, lastLastTransaction, lastTransaction, lastKeepAlive, lastPreLag, lastPing, lastPingDiff, lastTransactionfast, lastTransactionsFastSent, lastFlying, hitTime, velocityPing;

    private boolean lagging, spikeLag, totalLag, isReallySpiking;

    private int currentPing, lastLagTick = -2, lastFastTransactionPing, lastSpikeLagTick, keepALiveTicks;

    private double transactionFastLagThreshold, spikeLagThreshold;

    private HashMap<Integer, Long> spikeMap = new HashMap<>();


    public void update(Object packet, String type) {
        if (user != null) {


            //Skipped ticks
            if (type.equalsIgnoreCase(Packet.Client.POSITION_LOOK) || type.equalsIgnoreCase(Packet.Client.LOOK) || type.equalsIgnoreCase(Packet.Client.FLYING) || type.equalsIgnoreCase(Packet.Client.POSITION)) {
                lastFlying = System.currentTimeMillis();
            }

            //Faster transaction check because the old one was "not working"
            if (type.equalsIgnoreCase(Packet.Client.LOOK) || type.equalsIgnoreCase(Packet.Client.FLYING) || type.equalsIgnoreCase(Packet.Client.POSITION) || type.equalsIgnoreCase(Packet.Client.POSITION_LOOK) || type.equalsIgnoreCase(Packet.Client.KEEP_ALIVE) || type.equalsIgnoreCase(Packet.Client.TRANSACTION) || type.equalsIgnoreCase(Packet.Server.KEEP_ALIVE) || type.equalsIgnoreCase(Packet.Server.TRANSACTION)) {

                //Set the boolean 'totalLag' if they are normally lagging or spiking

                if (lagging || isReallySpiking) {
                    totalLag = true;
                } else {
                    totalLag = false;
                }

                if (transactionFastLagThreshold > 10) {
                    lastSpikeLagTick = user.getConnectedTick();
                }

                int currentSpikeDifference = Math.abs(lastSpikeLagTick - user.getConnectedTick());

                //Calulcate if they did spike in the connection
                if (currentSpikeDifference < 100 && user.getConnectedTick() > 105) {
                    spikeLag = true;
                } else {
                    spikeLag = false;
                }
            }

            if (type.equalsIgnoreCase(Packet.Client.KEEP_ALIVE)) {

                //Keepalive ping check

                long ping = (System.currentTimeMillis() - lastKeepAlive);

                lastPingDiff = Math.abs(ping - lastPing);

                lastPing = ping;

                currentPing = (int) (System.currentTimeMillis() - lastKeepAlive);

            }

            if (type.equalsIgnoreCase(Packet.Server.KEEP_ALIVE)) {

                //Transaction ping check

                //We add the id's to a map so if the player doesn't send one back that is correct we know they are trying to fake them

                short id = (short) (user.getMiscData().randomTransactionID() + user.getMiscData().getTransactionID());
                user.getTransactionMap().put(id, System.currentTimeMillis());
                user.getMiscData().setTransactionID(id);

                WrappedOutTransaction wrappedOutTransaction = new WrappedOutTransaction(0, id, false);
                TinyProtocolHandler.getInstance().getChannel().sendPacket(user.getPlayer(), wrappedOutTransaction.getObject());

                if (user.getTransactionMap().size() > 30) {
                    user.getTransactionMap().clear();
                }

                lastKeepAlive = System.currentTimeMillis();
            }

            if (type.equalsIgnoreCase(Packet.Client.TRANSACTION)) {

                //Magic is here, we difference the transaction ping between the current and last to see if they are spiking in connection to the server

                WrappedInTransactionPacket wrappedInTransactionPacket = new WrappedInTransactionPacket(packet, user.getPlayer());
                short id = wrappedInTransactionPacket.getAction();
                short currentID = user.getMiscData().getTransactionID();

                if (id == user.getMiscData().getTransactionFastID()) {

                    long transactionDifference = (System.currentTimeMillis() - lastTransactionfast);
                    long lastSentDifference = (System.currentTimeMillis() - lastTransactionsFastSent);

                    int ping = (int) Math.abs(transactionDifference - lastSentDifference);

                    int totalLag = Math.abs(ping - lastFastTransactionPing);

                    if (totalLag > 9) {
                        if (transactionFastLagThreshold < 100.0) transactionFastLagThreshold+=1.00;
                    } else {
                        if (transactionFastLagThreshold > 0.0) transactionFastLagThreshold-=1.00;
                    }


                    lastFastTransactionPing = ping;

                    lastTransactionfast = System.currentTimeMillis();
                }

                if (id == currentID && user.getTransactionMap().containsKey(id)) {

                    lastLastTransaction = lastTransaction;
                    lastTransaction = (System.currentTimeMillis() - lastKeepAlive);

                    long diff = Math.abs(lastLastTransaction - lastTransaction);

                    this.lastTransactionDiffPing = diff;

                    //Check if the differnce is > 100 to set their lag 'pre-lag' time

                    if (user.isSafe() && diff > 99L) {
                        lastPreLag = System.currentTimeMillis();
                        lastLagTick = user.getConnectedTick();
                        lagging = true;
                    }

                    if (user.isSafe() && diff > 100L && (lastPingDiff >= diff || Math.abs(diff - lastPingDiff) < 20)) {
                        lastPreLag = System.currentTimeMillis();
                        lastLagTick = user.getConnectedTick();
                    }

                }

                if ((System.currentTimeMillis() - lastPreLag) < 1000L) {
                    lastLagTick = user.getConnectedTick();
                }

                lagging = lastLagTick > -1 && Math.abs(user.getConnectedTick() - lastLagTick) < 101;

                user.getMiscData().setHasLagged(lagging);
            }
        }
    }
}
