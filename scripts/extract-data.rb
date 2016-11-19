Spell = Struct.new(:source) do
  def lines
    @lines ||= source.split(/\n+/)
  end

  def name
    lines[2]
  end

  def level
    cantrip? ? 0 : level_and_school_line.to_i
  end

  def school
    level_and_school_line.split[cantrip? ? 0 : 1].downcase
  end

  def casting_time
    if casting_time_line_collapsed?
      casting_time_line.match(/.*\* (.*?) R/).captures[0]
    else
      lines[6].split('*').last.strip
    end
  end

  def range
    if casting_time_line_collapsed?
    end
  end

  private

  def casting_time_line
    lines[6]
  end

  def casting_time_line_collapsed?
    casting_time_line.include? 'Range'
  end

  def level_and_school_line
    lines[4]
  end

  def cantrip?
    level_and_school_line.include? 'cantrip'
  end
end

CharacterClass = Struct.new(:name) do
  def filename
    "source/Spellcasting/by-class/#{name}.rst"
  end

  def spell_names
    File
      .readlines(filename)
      .slice_when { |s| s.include? 'Level' }
      .map { |a| a.select { |s| s[0] == '-' } }
      .drop(1)
      .map { |level| level.map { |spell| spell.match(/`srd:(.*?)`/).captures.first } }
  end

  def spell_sources
    spell_names.map { |level| level.map { |spell_name|
      File.read("source/Spellcasting/spells_a-z/#{spell_name[0]}/#{spell_name}.rst")
    } }
  end

  def spells
    spell_sources.map { |level| level.map { |src| Spell.new(src) } }
  end
end
