/*******************************************************************************
 * Copyright (c) 2010-2011 Obeo. All Rights Reserved.
 *
 * This file is part of Obeo Agility.
 *
 * This software and the attached documentation are the exclusive ownership
 * of its authors and was conceded to the profit of Obeo SARL.
 * This software and the attached documentation are protected under the rights
 * of intellectual ownership, including the section "Titre II  Droits des auteurs (Articles L121-1 L123-12)"
 * By installing this software, you acknowledge being aware of this rights and
 * accept them, and as a consequence you must:
 * - be in possession of a valid license of use conceded by Obeo only.
 * - agree that you have read, understood, and will comply with the license terms and conditions.
 * - agree not to do anything that could conflict with intellectual ownership owned by Obeo or its beneficiaries
 * or the authors of this software
 *
 * Should you not agree with these terms, you must stop to use this software and give it back to its legitimate owner.
 *
 * Acceleo and Obeo are trademarks owned by Obeo.
 *
 *******************************************************************************/

package dk.brics.automaton.test;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.RunAutomaton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class Main {

	private static final int NUMBER_LOOPS = 1000000;

	private static final String PATTERN = "(([^:]+)://)?([^:/]+)(:([0-9]+))?(/.*)";

	private static final String string = "{1: this is some more text - and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more this is some more text and some more and some more and even more at the end -}";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RegExp regExp = new RegExp(PATTERN);
		Pattern pattern = Pattern.compile(PATTERN);
		Automaton automaton = regExp.toAutomaton();
		RunAutomaton runAutomaton = new RunAutomaton(automaton);

		long start = System.currentTimeMillis();
		for (int i = 0; i < NUMBER_LOOPS; i++) {
			runAutomaton.run(string);
		}
		System.out.println("Run automaton : " + (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		for (int i = 0; i < NUMBER_LOOPS; i++) {
			Matcher matcher = pattern.matcher(string);
			matcher.matches();
		}
		System.out.println("Pattern : " + (System.currentTimeMillis() - start));
	}

}
