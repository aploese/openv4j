/*
 * OpenV4J - Drivers for the Viessmann optolink protocol https://github.com/openv/openv/wiki
 * Copyright (C) 2024, Arne Plöse and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package de.ibapl.openv4j.api.devices.v200kw2;

import de.ibapl.openv4j.api.tag.Name;
import de.ibapl.openv4j.api.tag.Trending;
import de.ibapl.openv4j.spi.protocolhandlers.MemArea;
import de.ibapl.openv4j.spi.protocolhandlers.MemoryImage;

/**
 *
 * @author aploese
 */
public class BoilerTemperatures_0x0800_0x1c extends MemArea {

    public BoilerTemperatures_0x0800_0x1c(MemoryImage mem) {
        super(mem, 0x0800, (short) 0x1c);
    }

    @Name("ADC_IstTemperaturwert_ATS~0x0800")
    public float getMeasuredOutsideTemperature() {
        return mem.getShort(baseAddress) * 0.1f;
    }

    @Name("ADC_Isttemperaturwert_KTS~0x0802")
    public float getMeasuredBoilerTemperature() {
        return mem.getShort(baseAddress + 0x02) * 0.1f;
    }

    @Name("ADC_IstTemperaturwert_STS1~0x0804")
    public float getMeasuredDhwTemperature() {
        return mem.getShort(baseAddress + 0x04) * 0.1f;
    }

    @Name("ADC_IstTemperaturwert_AGTS~0x0808")
    public float getMeasuredExhaustTemperature() {
        return mem.getShort(baseAddress + 0x08) * 0.1f;
    }

    @Trending
    @Name("TiefpassTemperaturwert_KTS~0x0810")
    public float getBoilerTemperature() {
        return mem.getByte(baseAddress + 0x10);
    }

    @Trending
    @Name("TiefpassTemperaturwert_STS1~0x0812")
    public float getDhwTemperature() {
        return mem.getByte(baseAddress + 0x12) * 0.1f;
    }

    @Trending
    @Name("TiefpassTemperaturwert_AGTS~0x0816")
    public float getExhaustTemperature() {
        return mem.getByte(baseAddress + 0x16) * 0.1f;
    }

    @Name("TiefpassTemperaturwert_17_NR1~0x0818")
    public float getSensor17Temperature() {
        return mem.getByte(baseAddress + 0x18) * 0.1f;
    }

    @Name("TiefpassTemperaturwert_2_NR1~0x081A")
    public float getSensor2Temperature() {
        return mem.getByte(baseAddress + 0x1a) * 0.1f;
    }

}
