/*
GrasslessDirtBackport Minecraft Mod
Copyright (C) 2016 Joseph C. Sible

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/

package josephcsible.grasslessdirtbackport;

import java.util.Map;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@MCVersion("1.7.10")
public class GdbpLoadingPlugin implements IFMLLoadingPlugin {

	// XXX this feels hacky. Is this really the best way to keep track of this?
	public static boolean runtimeDeobfuscationEnabled;

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{GdbpClassTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		runtimeDeobfuscationEnabled = (Boolean) data.get("runtimeDeobfuscationEnabled");
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
