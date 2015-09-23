package org.wesnoth.network.serializer;

import org.wesnoth.wml.BooleanWMLAttribute;
import org.wesnoth.wml.StringWMLAttribute;
import org.wesnoth.wml.WesnothSerializable;

import java.io.*;

public class Login implements WesnothSerializable {


    private String username = "BeoXTC";
    private boolean selective_ping = true;


    @Override
    public void writeExternal(OutputStream out) throws IOException {
        WMLTag wmlTag = new WMLTag(Login.class);
        wmlTag.putAttribute("selective_ping", new BooleanWMLAttribute(true));
        wmlTag.putAttribute("username", new StringWMLAttribute("BeoXTC"));

        wmlTag.writeExternal(out);
    }

    @Override
    public void readExternal(InputStream in) throws IOException, ClassNotFoundException {

    }
}
