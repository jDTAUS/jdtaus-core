<?xml version="1.0" encoding="UTF-8"?>
<!--

  ${project.name}
  Copyright (c) 2005 Christian Schulte

  Christian Schulte, Haldener Strasse 72, 58095 Hagen, Germany
  <schulte2005@users.sourceforge.net> (+49 2331 3543887)

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public
  License along with this library; if not, write to the Free Software
  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

-->
<xsl:stylesheet xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:container="http://jdtaus.org/core/model/container"
                version="1.0">

  <!-- No-op test stylesheet. -->

  <xsl:output method="xml" indent="yes" omit-xml-declaration="no"
              encoding="UTF-8" standalone="no"/>

  <xsl:template match="node()|@*">
    <xsl:copy>
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>
