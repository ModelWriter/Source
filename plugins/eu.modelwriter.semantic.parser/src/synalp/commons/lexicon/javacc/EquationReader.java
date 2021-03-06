/* Generated By:JavaCC: Do not edit this line. EquationReader.java */
package synalp.commons.lexicon.javacc;

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
public class EquationReader implements EquationReaderConstants {
        /**
	 * The prefix of the symbols that denote variables.
	 */
        public static String VARIABLE_PREFIX = "?";


        public static Equations readEquations(String content) throws Exception
        {
                return parser(content).Equations();
        }

        /**
		Creates a parser for given String.
	*/
        private static EquationReader parser(String str)
        {
                return new EquationReader(new ByteArrayInputStream(str.getBytes()));
        }

  final public Equations Equations() throws ParseException {
        Equation eq;
        Equations ret = new Equations();
    label_1:
    while (true) {
      eq = Equation();
                          ret.add(eq);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ID:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
    }
                {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

  final public Equation Equation() throws ParseException {
        Token nodeId;
        Token fsTypeToken;
        FeatureStructureType fsType;
        FeatureStructure fs;
    nodeId = jj_consume_token(ID);
    jj_consume_token(DOT);
    fsTypeToken = jj_consume_token(FSTYPE);
    jj_consume_token(10);
    fs = FlatFS();
                fsType = fsTypeToken.image.equals("top") ? FeatureStructureType.TOP : FeatureStructureType.BOTTOM;
                {if (true) return new Equation(nodeId.image, fsType, fs);}
    throw new Error("Missing return statement in function");
  }

// we should have the ? global variable prefix here also in parsing!
  final public FeatureValue Argument() throws ParseException {
        Token arg;
        FeatureValue ret;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 11:
      ret = Variable();
      break;
    case ID:
      ret = ConstantPipeSyntax();
      break;
    case 13:
      ret = ConstantCurlyBracketSyntax();
      break;
    default:
      jj_la1[1] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
                {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

  final public FeatureVariable Variable() throws ParseException {
        Token arg;
    jj_consume_token(11);
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
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 12:
        ;
        break;
      default:
        jj_la1[2] = jj_gen;
        break label_2;
      }
      jj_consume_token(12);
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
    jj_consume_token(13);
    arg = jj_consume_token(ID);
                         values.add(arg.image.trim());
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        jj_la1[3] = jj_gen;
        break label_3;
      }
      jj_consume_token(COMMA);
      arg = jj_consume_token(ID);
                                                                         values.add(arg.image.trim());
    }
    jj_consume_token(14);
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
      jj_la1[4] = jj_gen;
      ;
    }
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        jj_la1[5] = jj_gen;
        break label_4;
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
    jj_consume_token(15);
    ret = FlatFSNoBracket();
    jj_consume_token(16);
                {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

// a flat feature is a non-recursive feat
  final public Feature FlatFeature() throws ParseException {
        Token id;
        FeatureValue val;
    id = jj_consume_token(ID);
    jj_consume_token(10);
    val = Argument();
                {if (true) return new Feature(id.image, val);}
    throw new Error("Missing return statement in function");
  }

  /** Generated Token Manager. */
  public EquationReaderTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[6];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x200,0x2a00,0x1000,0x40,0x200,0x40,};
   }

  /** Constructor with InputStream. */
  public EquationReader(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public EquationReader(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new EquationReaderTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
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
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public EquationReader(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new EquationReaderTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public EquationReader(EquationReaderTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(EquationReaderTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
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

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[17];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 6; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 17; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
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

}
