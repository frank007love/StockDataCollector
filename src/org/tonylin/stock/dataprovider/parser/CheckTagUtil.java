package org.tonylin.stock.dataprovider.parser;

import org.htmlparser.Node;
import org.htmlparser.Tag;

public class CheckTagUtil {

	static public boolean isCorrespondingNode(Node node, String targetText){
		return isCorrespondingNode(node, new String[]{targetText});
	}
	
	static public boolean isCorrespondingNode(Node node, String[] targetTexts){
		if( node == null )
			return false;
		String text = node.getText();
		for( String targetText : targetTexts ){
			if( !text.contains(targetText) ){
				return false;
			}
		}
		return true;
	}
	
	static public boolean isCorrespondingTag(Tag tag, String targetTagName){
		if( tag == null || tag.getTagName() == null ) 
			return false;
		return tag.getTagName().equalsIgnoreCase(targetTagName);
	}
	
	static public boolean isCorrespondingTag(Tag tag, String targetTagName, String targetClassName){
		if( !isCorrespondingTag(tag, targetTagName) ){
			return false;
		}
		String className = tag.getAttribute("class");
		if( className == null || !className.equalsIgnoreCase(targetClassName) ){
			return false;
		}
		return true;
	}
}
