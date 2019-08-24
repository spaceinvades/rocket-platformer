/*
 * Decompiled with CFR 0_125.
 *
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.spaceinvds.handlers;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;

public class B2DTiledBoxMaker implements Disposable {
    private int tic;
    private int firstTile;
    private float th;
    private float tw;
    private Body body;
    private TiledMapTileLayer layer;
    private World world;
    private final float ppm;
    private BodyDef bdef;
    private FixtureDef fdef;
    private boolean noneNull;
    private String[] nulls;

    public B2DTiledBoxMaker(TiledMapTileLayer layer, World world, float ppm, BodyDef bdef, FixtureDef fdef) {
        nulls = new String[6];
        noneNull = true;
        if (layer != null) this.layer = layer;
        else {
            noneNull = false;
            nulls[0] = "Layer";
        }
        if (world != null) this.world = world;
        else {
            noneNull = false;
            nulls[1] = "World";
        }
        this.ppm = ppm;
        if (bdef != null) this.bdef = bdef;
        else {
            noneNull = false;
            nulls[2] = "Body Def";
        }
        if (fdef != null) this.fdef = fdef;
        else {
            noneNull = false;
            nulls[3] = "Fixture Def";
        }
        if (layer != null) this.th = layer.getTileHeight();
        else {
            noneNull = false;
            nulls[4] = "Tile Height";
        }
        if (layer != null) this.tw = layer.getTileWidth();
        else {
            noneNull = false;
            nulls[5] = "Tile Width";
        }
        this.firstTile = -1;
        this.tic = 0;
        if (bdef != null)
            bdef.position.set(0.0f, 0.0f);
        if (bdef != null) {
            assert world != null;
            this.body = world.createBody(bdef);
        }
    }

    public void create() {
        if (noneNull) {
            int row = 0;
            while (row <= layer.getHeight()) {
                for (int col = 0; col <= layer.getWidth(); ++col) {
                    float tiledMapScale = 2;
                    if (layer.getCell(col, row) != null) {
                        if (layer.getCell(col, row).getTile().getProperties().containsKey("wall")) {
                            bdef.position.set(((float) col + 0.5f) * tw / ppm,
                                    ((float) row + 0.5f) * tw / ppm);
                            Vector2[] chains = new Vector2[4];
                            chains[3] = new Vector2((-tw) / ppm / tiledMapScale,
                                    th / ppm / tiledMapScale);
                            chains[2] = new Vector2((-tw) / ppm / tiledMapScale,
                                    (-th) / ppm / tiledMapScale);
                            chains[1] = new Vector2(tw / ppm / tiledMapScale,
                                    th / ppm / tiledMapScale);
                            chains[0] = new Vector2(tw / ppm / tiledMapScale,
                                    (-th) / ppm / tiledMapScale);
                            ChainShape cs = new ChainShape();
                            cs.createChain(chains);
                            fdef.shape = cs;
                            world.createBody(bdef).createFixture(fdef).setUserData("platform");
                            cs.dispose();
                            continue;
                        }
                    } else {
                        if (layer.getCell(col, row) == null && tic <= 0)
                            continue;
                        if (layer.getCell(col, row) == null && tic > 0) {
                            PolygonShape shape = new PolygonShape();
                            shape.setAsBox((float) (col - firstTile) / ppm * tw / tiledMapScale,
                                    th / ppm / tiledMapScale,
                                    new Vector2((float) (firstTile + col) / ppm / tiledMapScale * tw,
                                            ((float) row + 0.5f) / ppm * th),
                                    0.0f);
                            fdef.shape = shape;
                            body.createFixture(fdef).setUserData("platform");
                            tic = 0;
                            firstTile = -1;
                            shape.dispose();
                            continue;
                        }
                    }
                    tic++;
                    if (firstTile != -1)
                        continue;
                    firstTile = col;
                }
                row++;
            }
        } else {
            System.out.println("\nBox2d Tiled Maker fields are null! Cannot make hitboxes! \nNull fields are: \n");
            for (String aNull : nulls) {
                if (aNull != null) {
                    System.out.println(aNull);
                }
            }
            System.out.println();
        }
    }

    public TiledMapTileLayer getLayer() {
        return this.layer;
    }

    public void remove() {
        this.world.destroyBody(this.body);
        this.dispose();
    }

    @Override
    public void dispose() {
        world = null;
        bdef = null;
        fdef = null;
        layer = null;
        body = null;
    }
}