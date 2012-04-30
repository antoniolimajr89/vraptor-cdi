package org.vraptor.impl;

import javax.enterprise.inject.Produces;

import org.vraptor.extension.RemovableControllerExtension;

public class DefaultRemovableControllerExtensions {

	@Produces @RemovableControllerExtension 
	String CONTROLLER_EXTENSION = "Controller";
	
	@Produces @RemovableControllerExtension 
	String ACTION_EXTENSION = "Action";
	
}
