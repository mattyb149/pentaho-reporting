package org.pentaho.reporting.engine.classic.extensions.datasources.mondrian.parser;

import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.libraries.xmlns.parser.AbstractXmlReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.StringReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.XmlReadHandler;
import org.pentaho.reporting.libraries.xmlns.parser.ParseException;
import org.pentaho.reporting.engine.classic.extensions.datasources.mondrian.CubeFileProvider;
import org.pentaho.reporting.engine.classic.extensions.datasources.mondrian.DefaultCubeFileProvider;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

public class DefaultCubeFileProviderReadHandler extends AbstractXmlReadHandler implements CubeFileProviderReadHandler
{
  private StringReadHandler pathReadHandler;

  public DefaultCubeFileProviderReadHandler()
  {
  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   * @throws SAXException if there is a parsing error.
   */
  protected XmlReadHandler getHandlerForChild(final String uri,
                                              final String tagName,
                                              final Attributes atts)
      throws SAXException
  {
    if (isSameNamespace(uri) == false)
    {
      return null;
    }
    if ("cube-filename".equals(tagName))
    {
      pathReadHandler = new StringReadHandler();
      return pathReadHandler;
    }
    return null;
  }

  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void doneParsing() throws SAXException
  {
    if (pathReadHandler == null)
    {
      throw new ParseException("Required element 'cube-filename' is not present.", getLocator());
    }
  }

  /**
   * Returns the object for this element or null, if this element does
   * not create an object.
   *
   * @return the object.
   * @throws org.xml.sax.SAXException if an parser error occured.
   */
  public Object getObject() throws SAXException
  {
    return getProvider();
  }

  protected String getPath()
  {
    return pathReadHandler.getResult();
  }

  public CubeFileProvider getProvider()
  {
    final DefaultCubeFileProvider fileProvider =
        ClassicEngineBoot.getInstance().getObjectFactory().get(DefaultCubeFileProvider.class);
    fileProvider.setMondrianCubeFile(pathReadHandler.getResult());
    return fileProvider;
  }
}
