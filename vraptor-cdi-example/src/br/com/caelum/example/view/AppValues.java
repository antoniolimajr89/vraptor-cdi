package br.com.caelum.example.view;

import org.vraptor.ViewHelper;

@ViewHelper
public class AppValues {

	public String getSomething() {
		return "[ this came from ViewHelper directly ]";
	}
}
