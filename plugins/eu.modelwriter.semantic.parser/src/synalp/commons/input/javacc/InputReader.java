/* Generated By:JavaCC: Do not edit this line. InputReader.java */
package synalp.commons.input.javacc;

import java.io.*;
import java.util.*;
import synalp.commons.input.*;
import synalp.commons.lexicon.*;
import synalp.commons.semantics.*;
import synalp.commons.unification.*;

/**
* An InputReader reads inputs by parsing them with Javacc.
*/
@SuppressWarnings("all")
public class InputReader implements InputReaderConstants {
        /**
	 * The prefix of the symbols that denote variables.
	 */
        public static String VARIABLE_PREFIX = "?";

        public static TestSuite readTestSuite(String content) throws Exception
        {
                return parser(content).TestSuite();
        }

        public static TestSuiteEntry readTestSuiteEntry(String content) throws Exception
        {
                return parser(content).TestSuiteEntry();
        }

        public static DefaultLiteral readLiteral(String content) throws Exception
        {
                return parser(content).Literal();
        }

        public static Semantics readSemantics(String content) throws Exception
        {
                return parser(content).Semantics();
        }


        /**
		Creates a parser for given String.
	*/
        private static InputReader parser(String str)
        {
                return new InputReader(new ByteArrayInputStream(str.getBytes()));
        }

  final public TestSuite TestSuite() throws ParseException {
        boolean first=false;
        boolean only=false;
        boolean strict=false;
        boolean morph = false;
        boolean ignore=false;
        boolean start=false;
        TestSuiteEntry entry;
        TestSuite ret = new TestSuite();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 11:
        jj_consume_token(11);
                      start=true;
        break;
      default:
        jj_la1[0] = jj_gen;
        ;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 12:
        jj_consume_token(12);
                       ignore=true;
        break;
      default:
        jj_la1[1] = jj_gen;
        ;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 13:
        jj_consume_token(13);
                      first=true;
        break;
      default:
        jj_la1[2] = jj_gen;
        ;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 14:
        jj_consume_token(14);
                     only=true;
        break;
      default:
        jj_la1[3] = jj_gen;
        ;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 15:
        jj_consume_token(15);
                      morph=true;
        break;
      default:
        jj_la1[4] = jj_gen;
        ;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 16:
        jj_consume_token(16);
                     {if (true) return ret;}
        break;
      default:
        jj_la1[5] = jj_gen;
        ;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 17:
        jj_consume_token(17);
                      strict=true;
        break;
      default:
        jj_la1[6] = jj_gen;
        ;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 18:
        jj_consume_token(18);
                     strict=false;
        break;
      default:
        jj_la1[7] = jj_gen;
        ;
      }
      // this is the default when none are set and used only to explicit it
              entry = TestSuiteEntry();
          if (start)
             ret.clear();

          if (!ignore)
          {
            if (strict)
                entry.setStrict(true);
            if (morph)
                entry.setMorph(true);
            if (only)
                  {
                        ret = new TestSuite();
                        ret.add(entry);
                        {if (true) return ret;}
                  }
                if (first)
                        ret.add(0, entry);
                else ret.add(entry);
          }

                first=false;
                strict=false;
                only=false;
                ignore=false;
                morph=false;
                start=false;
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ID:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 17:
      case 18:
        ;
        break;
      default:
        jj_la1[8] = jj_gen;
        break label_1;
      }
    }
                {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

  final public TestSuiteEntry TestSuiteEntry() throws ParseException {
        Token id;
        Semantics semantics;
        List<String> sentences = new ArrayList<String>();
    id = jj_consume_token(ID);
    jj_consume_token(19);
    semantics = Semantics();
    jj_consume_token(20);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 21:
      jj_consume_token(21);
      sentences = Sentences();
      break;
    default:
      jj_la1[9] = jj_gen;
      ;
    }
                {if (true) return new TestSuiteEntry(id.image, semantics, sentences);}
    throw new Error("Missing return statement in function");
  }

// the free text is split around ",", we maybe could do it differently, I did it this way to prevent clash between <ID> and <TEXT>
  final public List<String > Sentences() throws ParseException {
        Token token=null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case TEXT:
      token = jj_consume_token(TEXT);
      break;
    default:
      jj_la1[10] = jj_gen;
      ;
    }
                if (token==null)
                        {if (true) return new ArrayList<String>();}

                String content = token.image.substring(1).substring(0, token.image.length()-2);
                List<String > ret = new ArrayList<String>();
                for(String str : content.split(","))
                        ret.add(str.trim());
                {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

  final public Semantics Semantics() throws ParseException {
        DefaultLiteral literal;
        Semantics semantics = new Semantics();
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ID:
      case 22:
      case 26:
      case 28:
        ;
        break;
      default:
        jj_la1[11] = jj_gen;
        break label_2;
      }
      literal = Literal();
                               semantics.add(literal);
    }
          {if (true) return semantics;}
    throw new Error("Missing return statement in function");
  }

// parses a literal that only contains constants or variables
  final public DefaultLiteral Literal() throws ParseException {
        FeatureValue arg = null;
        FeatureValue label = null;
        FeatureValue predicate = null;
        boolean selectional = false;
        List<FeatureValue> args = new ArrayList<FeatureValue>();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 22:
      jj_consume_token(22);
              selectional=true;
      break;
    default:
      jj_la1[12] = jj_gen;
      ;
    }
    if (jj_2_1(2)) {
      label = Argument();
      jj_consume_token(23);
    } else {
      ;
    }
    predicate = Argument();
    jj_consume_token(24);
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ID:
      case 26:
      case 28:
        ;
        break;
      default:
        jj_la1[13] = jj_gen;
        break label_3;
      }
      arg = Argument();
                                                                                                                   args.add(arg);
    }
    jj_consume_token(25);
                DefaultLiteral ret = new DefaultLiteral();
                if (label!=null)
                        ret.setLabel(label);
                ret.setPredicate(predicate);
                ret.setArguments(args);
                ret.setSelectional(selectional);
                {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

// we should have the ? global variable prefix here also in parsing!
  final public FeatureValue Argument() throws ParseException {
        Token arg;
        FeatureValue ret;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 26:
      ret = Variable();
      break;
    case ID:
      ret = ConstantPipeSyntax();
      break;
    case 28:
      ret = ConstantCurlyBracketSyntax();
      break;
    default:
      jj_la1[14] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
                {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

  final public FeatureVariable Variable() throws ParseException {
        Token arg;
    jj_consume_token(26);
    arg = jj_consume_token(ID);
                       {if (true) return new FeatureVariable(VARIABLE_PREFIX+arg.image.trim());}
    throw new Error("Missing return statement in function");
  }

// a Constant with syntax: a|b|c
  final public FeatureConstant ConstantPipeSyntax() throws ParseException {
        Token arg;
        Set<String > values = new HashSet<String>();
    arg = jj_consume_token(ID);
                     values.add(arg.image.trim());
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 27:
        ;
        break;
      default:
        jj_la1[15] = jj_gen;
        break label_4;
      }
      jj_consume_token(27);
      arg = jj_consume_token(ID);
                                                                     values.add(arg.image.trim());
    }
                {if (true) return new FeatureConstant(values);}
    throw new Error("Missing return statement in function");
  }

// a Constant with syntax: {a,b,c}
  final public FeatureConstant ConstantCurlyBracketSyntax() throws ParseException {
        Token arg;
        Set<String > values = new HashSet<String>();
    jj_consume_token(28);
    arg = jj_consume_token(ID);
                         values.add(arg.image.trim());
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        jj_la1[16] = jj_gen;
        break label_5;
      }
      jj_consume_token(COMMA);
      arg = jj_consume_token(ID);
                                                                         values.add(arg.image.trim());
    }
    jj_consume_token(29);
                {if (true) return new FeatureConstant(values);}
    throw new Error("Missing return statement in function");
  }

// a flat FS without [], does not contain recursive features
  final public FeatureStructure FlatFSNoBracket() throws ParseException {
        Feature feat;
        FeatureStructure ret = new FeatureStructure();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ID:
      feat = FlatFeature();
                             ret.add(feat);
      break;
    default:
      jj_la1[17] = jj_gen;
      ;
    }
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        jj_la1[18] = jj_gen;
        break label_6;
      }
      jj_consume_token(COMMA);
      feat = FlatFeature();
                                                                            ret.add(feat);
    }
                {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

// a flat FS is enclosed in [], does not contain recursive features
  final public FeatureStructure FlatFS() throws ParseException {
        FeatureStructure ret = new FeatureStructure();
    jj_consume_token(30);
    ret = FlatFSNoBracket();
    jj_consume_token(20);
                {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

// a flat feature is a non-recursive feat
  final public Feature FlatFeature() throws ParseException {
        Token id;
        FeatureValue val;
    id = jj_consume_token(ID);
    jj_consume_token(31);
    val = Argument();
                {if (true) return new Feature(id.image, val);}
    throw new Error("Missing return statement in function");
  }

  private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  private boolean jj_3R_13() {
    if (jj_scan_token(28)) return true;
    if (jj_scan_token(ID)) return true;
    return false;
  }

  private boolean jj_3R_11() {
    if (jj_scan_token(26)) return true;
    if (jj_scan_token(ID)) return true;
    return false;
  }

  private boolean jj_3_1() {
    if (jj_3R_7()) return true;
    if (jj_scan_token(23)) return true;
    return false;
  }

  private boolean jj_3R_10() {
    if (jj_3R_13()) return true;
    return false;
  }

  private boolean jj_3R_8() {
    if (jj_3R_11()) return true;
    return false;
  }

  private boolean jj_3R_14() {
    if (jj_scan_token(27)) return true;
    return false;
  }

  private boolean jj_3R_7() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_8()) {
    jj_scanpos = xsp;
    if (jj_3R_9()) {
    jj_scanpos = xsp;
    if (jj_3R_10()) return true;
    }
    }
    return false;
  }

  private boolean jj_3R_12() {
    if (jj_scan_token(ID)) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_14()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  private boolean jj_3R_9() {
    if (jj_3R_12()) return true;
    return false;
  }

  /** Generated Token Manager. */
  public InputReaderTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  private int jj_gen;
  final private int[] jj_la1 = new int[19];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x800,0x1000,0x2000,0x4000,0x8000,0x10000,0x20000,0x40000,0x7fa00,0x200000,0x400,0x14400200,0x400000,0x14000200,0x14000200,0x8000000,0x40,0x200,0x40,};
   }
  final private JJCalls[] jj_2_rtns = new JJCalls[1];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  /** Constructor with InputStream. */
  public InputReader(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public InputReader(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new InputReaderTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public InputReader(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new InputReaderTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public InputReader(InputReaderTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(InputReaderTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      jj_entries_loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              continue jj_entries_loop;
            }
          }
          jj_expentries.add(jj_expentry);
          break jj_entries_loop;
        }
      }
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[32];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 19; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 32; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

  private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 1; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}
