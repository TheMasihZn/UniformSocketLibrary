package com.company.Atoms;

import java.io.Serializable;

public class DeviceInfo implements Serializable {
        private final String Name;

        public DeviceInfo(String name) {
            Name = name;
        }

        public String getName() {
            return Name;
        }

//        todo: add icon enum
    }