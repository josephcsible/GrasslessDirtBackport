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

import java.util.ListIterator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.tree.*;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.IClassTransformer;
import static org.objectweb.asm.Opcodes.*;

public class GdbpClassTransformer implements IClassTransformer {

	private void transformGetDamageValue(MethodNode mn) {
		/*
		Here's what we're doing to the bytecode:
		INVOKEVIRTUAL net/minecraft/world/World.getBlockMetadata (III)I
		*** remove everything starting here...
		ISTORE 5
		*** stuff omitted
		ILOAD 5
		*** ...and ending here
		IRETURN
		L4
		LOCALVARIABLE this Lnet/minecraft/block/BlockDirt; L0 L4 0
		LOCALVARIABLE p_149643_1_ Lnet/minecraft/world/World; L0 L4 1
		LOCALVARIABLE p_149643_2_ I L0 L4 2
		LOCALVARIABLE p_149643_3_ I L0 L4 3
		LOCALVARIABLE p_149643_4_ I L0 L4 4
		LOCALVARIABLE l I L1 L4 5 *** remove this too
		MAXSTACK = 4
		MAXLOCALS = 6 *** change to 5
		*/
		System.out.println("Patching getDamageValue");
		ListIterator<AbstractInsnNode> insnIter = mn.instructions.iterator();
		while(insnIter.next().getOpcode() != INVOKEVIRTUAL) {
			// do nothing
		}
		while(insnIter.next().getOpcode() != IRETURN) {
			insnIter.remove();
		}
		if(!mn.localVariables.isEmpty()) {
			// local variables information is optional, and apparently only dev builds have it
			ListIterator<LocalVariableNode> varIter = mn.localVariables.listIterator();
			while(varIter.next().index != 5) {
				// do nothing
			}
			varIter.remove();
		}
		mn.maxLocals = 5;
	}

	private void transformGetSubBlocks(MethodNode mn) {
		/*
		Here's what we're doing:
		p_149666_3_.add(new ItemStack(this, 1, 0));
		p_149666_3_.add(new ItemStack(this, 1, 1)); <-- we're adding this line
		p_149666_3_.add(new ItemStack(this, 1, 2));
		*/
		System.out.println("Patching getSubBlocks");
		ListIterator<AbstractInsnNode> insnIter = mn.instructions.iterator();
		AbstractInsnNode insn;

		// Save the class name of ItemStack for later, since it varies by obfuscation
		do {
			insn = insnIter.next();
		} while(insn.getOpcode() != NEW);
		String itemStackType = ((TypeInsnNode)insn).desc;

		// Save the descriptor of ItemStack's constructor for later, since it varies by obfuscation
		do {
			insn = insnIter.next();
		} while(insn.getOpcode() != INVOKESPECIAL);
		String itemStackInitDesc = ((MethodInsnNode)insn).desc;

		do {
			insn = insnIter.next();
		} while(insnIter.next().getOpcode() != POP);

		InsnList toInsert = new InsnList();
		toInsert.add(new LabelNode(new Label()));
		toInsert.add(new VarInsnNode(ALOAD, 3));
		toInsert.add(new TypeInsnNode(NEW, itemStackType));
		toInsert.add(new InsnNode(DUP));
		toInsert.add(new VarInsnNode(ALOAD, 0));
		toInsert.add(new InsnNode(ICONST_1));
		toInsert.add(new InsnNode(ICONST_1));
		toInsert.add(new MethodInsnNode(INVOKESPECIAL, itemStackType, "<init>", itemStackInitDesc, false));
		toInsert.add(new MethodInsnNode(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true));
		toInsert.add(new InsnNode(POP));
		mn.instructions.insertBefore(insnIter.next(), toInsert);
	}

	private static ClassNode byteArrayToClassNode(byte[] basicClass) {
		ClassNode cn = new ClassNode();
		ClassReader cr = new ClassReader(basicClass);
		cr.accept(cn, 0);
		return cn;
	}

	private static byte[] classNodeToByteArray(ClassNode cn) {
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		cn.accept(cw);
		return cw.toByteArray();
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		if(!transformedName.equals("net.minecraft.block.BlockDirt")) {
			return basicClass;
		}
		ClassNode cn = byteArrayToClassNode(basicClass);

		String getDamageValueName, getDamageValueDesc, getSubBlocksName, getSubBlocksDesc;
		if(GdbpLoadingPlugin.runtimeDeobfuscationEnabled) {
			getDamageValueName = "k";
			getDamageValueDesc = "(Lahb;III)I";
			getSubBlocksName = "a";
			getSubBlocksDesc = "(Ladb;Labt;Ljava/util/List;)V";
		} else {
			getDamageValueName = "getDamageValue";
			getDamageValueDesc = "(Lnet/minecraft/world/World;III)I";
			getSubBlocksName = "getSubBlocks";
			getSubBlocksDesc = "(Lnet/minecraft/item/Item;Lnet/minecraft/creativetab/CreativeTabs;Ljava/util/List;)V";
		}

		for(MethodNode mn : cn.methods) {
			if (mn.name.equals(getDamageValueName) && mn.desc.equals(getDamageValueDesc)) {
				transformGetDamageValue(mn);
			} else if (mn.name.equals(getSubBlocksName) && mn.desc.equals(getSubBlocksDesc)) {
				transformGetSubBlocks(mn);
			}
		}

		return classNodeToByteArray(cn);
	}
}
