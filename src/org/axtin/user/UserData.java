package org.axtin.user;

import org.axtin.container.facade.Container;
import org.axtin.user.role.DonatorRole;
import org.axtin.user.role.PrisonRole;
import org.axtin.user.role.StaffRole;

import java.math.BigDecimal;
import java.util.UUID;

public class UserData {
    private UUID uuid;
    private PrisonRole prisonRole;
    private PrisonRole mining;
    private DonatorRole donatorRole;
    private StaffRole staffRole;
    private double balance;
    private int tokens;

    public UserData(UUID uuid, PrisonRole prisonRole, DonatorRole donatorRole, StaffRole staffRole, double balance, int tokens) {
        this.uuid = uuid;
        this.prisonRole = prisonRole;
        this.donatorRole = donatorRole;
        this.staffRole = staffRole;
        this.balance = balance;
        this.tokens = tokens;
        this.mining = prisonRole;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public PrisonRole getPrisonRole() {
        return prisonRole;
    }

    public void setPrisonRole(PrisonRole prisonRole) {

        this.prisonRole = prisonRole;
    }

    public void setMining(PrisonRole b){
        this.mining = b;
    }

    public PrisonRole getMining(){
        return this.mining;
    }

    public DonatorRole getDonatorRole() {
        return donatorRole;
    }

    public void setDonatorRole(DonatorRole donatorRole) {

        this.donatorRole = donatorRole;
    }

    public StaffRole getStaffRole() {
        return staffRole;
    }

    public void setStaffRole(StaffRole staffRole) {

        this.staffRole = staffRole;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {

        this.balance = balance;
    }

    public int getTokens() {
        return tokens;
    }

    public void setTokens(int tokens) {

        this.tokens = tokens;
    }


}
