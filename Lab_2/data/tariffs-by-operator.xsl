<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" indent="yes" encoding="UTF-8"/>
    <xsl:strip-space elements="*"/>

    <xsl:key name="by-operator" match="Tariff" use="OperatorName"/>

    <xsl:template match="/Tariffs">
        <Operators>
            <xsl:for-each select="Tariff[generate-id() = generate-id(key('by-operator', OperatorName)[1])]">
                <xsl:sort select="OperatorName" lang="en"/>
                <Operator name="{OperatorName}">
                    <xsl:apply-templates select="key('by-operator', OperatorName)">
                        <xsl:sort select="Payroll" data-type="number"/>
                    </xsl:apply-templates>
                </Operator>
            </xsl:for-each>
        </Operators>
    </xsl:template>

    <xsl:template match="Tariff">
        <Tariff id="{@id}">
            <xsl:copy-of select="*[name() != 'OperatorName']"/>
        </Tariff>
    </xsl:template>
</xsl:stylesheet>
