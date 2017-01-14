package me.eddiep.bigdata.data;

import me.eddiep.jconfig.system.Config;
import me.eddiep.jconfig.system.annotations.DefaultValue;
import me.eddiep.jconfig.system.annotations.Getter;

public interface MongoConfig extends Config {

    @Getter(property = "ip")
    @DefaultValue(value = "127.0.0.1")
    String getIp();

    @Getter(property = "port")
    @DefaultValue(value = "27017")
    int getPort();

    @Getter(property = "database")
    @DefaultValue(value = "m")
    String getDatabaseName();
}
