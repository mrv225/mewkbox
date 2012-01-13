package mewkbot.entities;

/**
 *
 * @author Mewes
 */
public class User {
    private String nick = null;
    private boolean admin = false;
    private boolean operator = false;
    private float spamScore = 0;

    /**
     * @return the nick
     */
    public String getNick() {
        return nick;
    }

    /**
     * @param nick the nick to set
     */
    public void setNick(String nick) {
        this.nick = nick;
    }

    /**
     * @return the admin
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * @param admin the admin to set
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * @return the operator
     */
    public boolean isOperator() {
        return operator;
    }

    /**
     * @param operator the operator to set
     */
    public void setOperator(boolean operator) {
        this.operator = operator;
    }

    /**
     * @return the spamScore
     */
    public float getSpamScore() {
        return spamScore;
    }

    /**
     * @param spamScore the spamScore to set
     */
    public void setSpamScore(float spamScore) {
        this.spamScore = spamScore;
    }

}