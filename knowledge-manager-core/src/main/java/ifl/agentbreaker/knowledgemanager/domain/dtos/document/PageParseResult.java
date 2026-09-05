package ifl.agentbreaker.knowledgemanager.domain.dtos.document;

import lombok.Data;

import java.util.List;

// Single page parsing result.
@Data
public class PageParseResult
{
    private List<SectionTextBlock> sectionTextBlocks;

    private List<ParsedImage> parsedImages;

    private List<ParsedTable> parsedTables;
}
