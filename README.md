# RLMapEditor

Editor for creating 2D TileMaps (currently only supporting [Besmash](https://github.com/chaotx-eu/besmash))

## Important
The project is still in developement and may (will) contain bugs. Also any functionality
may change in the future plus there is no guaranty that old project saves will be compatible
with newer versions (if you're really desperate you can send me the project file and I see
what I can do but I cannot promise anything).

## Installing

### Download
You can grab an already compiled version [here](https://github.com/chaotx-eu/rlmeditor/releases).

### Deployement
Alternatively you can build the project by yourself, which requires [maven](https://maven.apache.org/).
```
## once maven is installed clone the project
git clone https://github.com/chaotx-eu/rlmeditor.git

## change to the project directory
cd rlmeditor

## build with maven jfx plugin
mvn jfx:jar
```
The executable jar will then be located in `rlmeditor/target/jfx/app/rlmeditor-x.y.z-jfx.jar`.

## Getting Started

It's an executable jar. You should know what to do.

Note: On first startup the application will create a folder with the name `.RLMEditor`
in your user directory. This folder is used for storing spritesheets, projects,
configuration files and similiar.

### Create a new Project (Map)
To create a new project select `File->New`.
In the following dialog you have to choose a *TileSet* to use for your map and define
the maps width and height.

Some demo TileSets may already be delivered with the application but it is highly encouraged
to create and use your own TileSets (see next point *Custom Content* for more info).

### Save Project
Select `File->SaveAs` to save the current project to a new file.
After entering and confirming a project name the project file will be located in
the `.RLMEditor/projects` folder.

If the project is already associated to a file (i.e. it was loaded or saved before
with *saveAs*) you can simpliy select `File->Save`.

### Load Project (Delete Project)
To load a project select `File->Load`. Projects can also be deleted in the
following dialog.

### Export Map
Click on *Export* in the bottom right corner. The map will be exported in to an xml
file in a format which is currently only supported by *Monogame*.

### Include Map in a Game
In your game project run the Monogame PipelineTool `./Content/Content.mgcb` and add the
exported xml file as item.

The SpriteSheet must also be added with the same same folder structure it was located
in the `.RLMEditor` folder, e.g. `Content/images/maps/some_sprite_sheet.png`.

You can create a TileMap-Object using the *ContentManager* e.g:
```
TileMap myTileMap = Content.Load<TileMap>("myTileMapFile");
```

## Custom Content

If you want to create your own TileSet all you need to do is to grab a *TileSheet*
in `.png` format and do some xml writing to define the single tiles.

### TileSheet
As mentioned the TileSheet has to be in `.png` format. It needs to be placed in
the `.RLMEditor/images/maps` folder.

### TileSet XML
The tileset `.xml` file has to be located in the `.RLMEditor/tilesets` folder.

The xml-syntax is as follows:
```xml
<?xml version="1.0" encoding="utf-8" ?>
<tileSet title>
	<sheet>some_tile_sheet.png</sheet>

	[<tile x y w[ h r solid title] />]*
	[
		<category title>
			[<tile x y w[ h r solid title] />]*
			[
				<subcategory title>
					[<tile x y w[ h r solid title] />]*
				</subcategory>
			]*
		</category>
	]*
</tileSet>
```

For example:
```xml
<?xml version="1.0" encoding="utf-8" ?>

<tileSet title="Demo Tile Set">
	<sheet>demo0_sheet.png</sheet>
	
	<category title="Floors">
		<tile title="Floor0" solid="false" x="0" y="0" w="16" h ="16" />
		<tile title="Floor1" solid="false" x="16" y="0" w="16" h ="16" />
		<tile title="Floor2" solid="false" x="32" y="0" w="16" h ="16" />
	</category>
	
	<category title="Walls">
		<subcategory title="Vertical">
			<tile title="WallV0" solid="true" x="0" y="16" w="16" h ="16" />
			<tile title="WallV1" solid="true" x="16" y="16" w="16" h ="16" />
			<tile title="WallV2" solid="true" x="32" y="16" w="16" h ="16" />
		</subcategory>
		
		<subcategory title="Horizontal">
			<tile title="WallH0" solid="true" x="0" y="32" w="16" h ="16" />
			<tile title="WallH1" solid="true" x="16" y="32" w="16" h ="16" />
			<tile title="WallH2" solid="true" x="32" y="32" w="16" h ="16" />
		</subcategory>
		
		<subcategory title="CrossSect">
			<tile title="Cross0" solid="true" x="0" y="48" w="16" h ="16" />
			<tile title="Cross1" solid="true" x="16" y="48" w="16" h ="16" />
			<tile title="Cross2" solid="true" x="32" y="48" w="16" h ="16" />
		</subcategory>
		
		<subcategory title="TSections">
			<tile title="TSectTop0" solid="true" x="96" y="0" w="16" h ="16" />
			<tile title="TSectTop1" solid="true" x="112" y="0" w="16" h ="16" />
			<tile title="TSectTop2" solid="true" x="128" y="0" w="16" h ="16" />
			<tile title="TSectLeft0" solid="true" x="96" y="16" w="16" h ="16" />
			<tile title="TSectLeft1" solid="true" x="112" y="16" w="16" h ="16" />
			<tile title="TSectLeft2" solid="true" x="128" y="16" w="16" h ="16" />
			<tile title="TSectBot0" solid="true" x="96" y="32" w="16" h ="16" />
			<tile title="TSectBot1" solid="true" x="112" y="32" w="16" h ="16" />
			<tile title="TSectBot2" solid="true" x="128" y="32" w="16" h ="16" />
			<tile title="TSectRight0" solid="true" x="96" y="48" w="16" h ="16" />
			<tile title="TSectRight1" solid="true" x="112" y="48" w="16" h ="16" />
			<tile title="TSectRight2" solid="true" x="128" y="48" w="16" h ="16" />
		</subcategory>
		
		<subcategory title="Corners">
			<tile title="CornerTL0" solid="true" x="48" y="0" w="16" h ="16" />
			<tile title="CornerTL1" solid="true" x="64" y="0" w="16" h ="16" />
			<tile title="CornerTL2" solid="true" x="80" y="0" w="16" h ="16" />
			<tile title="CornerBL0" solid="true" x="48" y="16" w="16" h ="16" />
			<tile title="CornerBL1" solid="true" x="64" y="16" w="16" h ="16" />
			<tile title="CornerBL2" solid="true" x="80" y="16" w="16" h ="16" />
			<tile title="CornerTR0" solid="true" x="48" y="32" w="16" h ="16" />
			<tile title="CornerTR1" solid="true" x="64" y="32" w="16" h ="16" />
			<tile title="CornerTR2" solid="true" x="80" y="32" w="16" h ="16" />
			<tile title="CornerBR0" solid="true" x="48" y="48" w="16" h ="16" />
			<tile title="CornerBR1" solid="true" x="64" y="48" w="16" h ="16" />
			<tile title="CornerBR2" solid="true" x="80" y="48" w="16" h ="16" />
		</subcategory>
	</category>
</tileSet>
```
