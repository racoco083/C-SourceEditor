package ceditor;
import javax.swing.*;
public class Model{
	private String[] keyword = {"int","#include","#define","void","double","float",
            "char","long","short","unsigned","struct","class","union",
            "enum","if","for","while","return","false","true","sizeof",
            "public","private","protected","else","typedef","#pragma",
            "fwrite","fread","warning","disable","bool","static","default",
            "const","switch","case","do","break","continue","#undef","#if",
            "#elseif","#else","#endif","#ifndef","#ifdef","inline","extern",
            "#import","#using","#error","#line","once"};
	
	private String[] header = {"<string.h>","<stdio.h>","<assert.h>","<complex.h>","<ctype.h>","<errno.h>","<fenv.h>",
							   "<float.h>","<inttypes.h>","<iso646.h>","<limits.h>","<locale.h>",
							   "<math.h>","<setjmp.h>","<signal.h>","<stdalign.h>","<stdarg.h>",
							   "<stdatomic.h>","<stdbool.h>","<stddef.h>","<stdint.h>",
							   "<stdlib.h>","<stdnoreturn.h>","<tgmath.h>","<threads.h>",
							   "<time.h>","<uchar.h>","<wchar.h>","<wctype.h>"};
	public String[] getk()
	{
		return keyword;
	}
	public String[] geth()
	{
		return header;
	}
}
