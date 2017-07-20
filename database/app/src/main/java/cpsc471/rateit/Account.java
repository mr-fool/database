/**
 * Created by Kyle on 6/10/2017.
 */

package cpsc471.rateit;

import static android.R.attr.id;

public class Account {
    private int _id;
    private String _username;
    private String _password;

    public Account() {

    }

    public Account(String username, String password) {
        this._username = username;
        this._password = password;
    }

    public void setUsername(String username) {
        this._username = username;
    }

    public String getUsername() {
        return this._username;
    }

    public void setPassword(String password) {
        this._password = password;
    }

    public String getPassword() {
        return this._password;
    }
}