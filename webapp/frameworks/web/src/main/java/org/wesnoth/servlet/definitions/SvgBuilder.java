package org.wesnoth.servlet.definitions;

import org.plutext.jaxb.svg11.*;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

public class SvgBuilder {

    private String version = "1.1";
    private String baseProfile = "full";
    private String height = "0px";
    private String width = "0px";
    private List<JAXBElement> elements = new ArrayList<>();

    public void withVersion(String version) {
        this.version = version;
    }

    public void withBaseProfile(String baseProfile) {
        this.baseProfile = baseProfile;
    }

    public void withHeight(String height) {
        this.height = height;
    }

    public void withWidth(String width) {
        this.width = width;
    }

    public Svg build() {
        ObjectFactory factory = new ObjectFactory();
        Svg svg = factory.createSvg();
        svg.setVersion(version);
        svg.setBaseProfile(baseProfile);
        svg.setHeight(height);
        svg.setWidth(width);
        svg.getSVGDescriptionClassOrSVGAnimationClassOrSVGStructureClass().addAll(elements);
        return svg;
    }

    public SvgTitleBuilder addTitle() {
        return new SvgTitleBuilder(this);
    }

    private static <T> JAXBElement<T> wrapWithJAXBElement(String tagName, T element) {
        QName tagQName = new QName(tagName);
        return new JAXBElement<T>(tagQName, (Class<T>) element.getClass(), element);
    }

    public SvgDefsBuilder addDefs() {
        return new SvgDefsBuilder(this);
    }

    public class SvgTitleBuilder {
        private String content;
        private SvgBuilder parent;

        public SvgTitleBuilder(SvgBuilder parent) {
            this.parent = parent;
        }

        public SvgTitleBuilder content(String content) {
            this.content = content;
            return this;
        }

        public SvgBuilder finish() {
            Title title = new ObjectFactory().createTitle();
            title.setContent(content);
            parent.elements.add(wrapWithJAXBElement("title", title));
            return parent;
        }
    }

    public class SvgDefsBuilder {
        private SvgBuilder parent;
        private List<JAXBElement> elements = new ArrayList<>();


        public SvgDefsBuilder(SvgBuilder parent) {
            this.parent = parent;
        }

        public SvgBuilder finish() {
            Defs defs = new ObjectFactory().createDefs();
            defs.getSVGDescriptionClassOrSVGAnimationClassOrSVGStructureClass().addAll(elements);
            parent.elements.add(wrapWithJAXBElement("defs", defs));
            return parent;
        }

        public SvgPatternBuilder addPattern() {
            return new SvgPatternBuilder(this);
        }

        public SvgPolygonBuilder addPolygon() {
            return new SvgPolygonBuilder(this);
        }

        public class SvgPatternBuilder {
            private SvgDefsBuilder parent;
            private String id;
            private String x;
            private String y;
            private String patternUnits;
            private String height;
            private String width;
            private List<JAXBElement> elements = new ArrayList<>();

            public SvgPatternBuilder(SvgDefsBuilder parent) {
                this.parent = parent;
            }

            public SvgPatternBuilder id(String id) {
                this.id = id;
                return this;
            }

            public SvgPatternBuilder x(String x) {
                this.x = x;
                return this;
            }

            public SvgPatternBuilder y(String y) {
                this.y = y;
                return this;
            }

            public SvgPatternBuilder patternUnits(String patternUnits) {
                this.patternUnits = patternUnits;
                return this;
            }

            public SvgPatternBuilder height(String height) {
                this.height = height;
                return this;
            }

            public SvgPatternBuilder width(String width) {
                this.width = width;
                return this;
            }

            public SvgImageBuilder addImage() {
                return new SvgImageBuilder(this);
            }

            public SvgDefsBuilder finish() {

                SVGPatternClass pattern = new ObjectFactory().createSVGPatternClass();
                pattern.setId(id);
                pattern.setHeight(height);
                pattern.setWidth(width);
                pattern.setX(x);
                pattern.setY(y);
                pattern.setPatternUnits(patternUnits);
                pattern.getSVGDescriptionClassOrSVGAnimationClassOrSVGStructureClass().addAll(elements);
                parent.elements.add(wrapWithJAXBElement("pattern", pattern));
                return parent;
            }

            public class SvgImageBuilder {
                private SvgPatternBuilder parent;
                private String height;
                private String width;
                private String href;

                public SvgImageBuilder(SvgPatternBuilder parent) {
                    this.parent = parent;
                }

                public SvgPatternBuilder finish() {

                    SVGImageClass image = new ObjectFactory().createSVGImageClass();
                    image.setHeight(height);
                    image.setWidth(width);
                    image.setHref(href);
                    parent.elements.add(wrapWithJAXBElement("image", image));
                    return parent;
                }

                public SvgImageBuilder height(String height) {
                    this.height = height;
                    return this;
                }

                public SvgImageBuilder width(String width) {
                    this.width = width;
                    return this;
                }

                public SvgImageBuilder href(String href) {
                    this.href = href;
                    return this;
                }
            }
        }

        public class SvgPolygonBuilder {
            private SvgDefsBuilder parent;
            private String id;
            private String clazz;
            private String points;
            private String fill;

            public SvgPolygonBuilder(SvgDefsBuilder parent) {
                this.parent = parent;
            }

            public SvgDefsBuilder finish() {
                Polygon polygon = new ObjectFactory().createPolygon();
                polygon.setId(id);
                polygon.setClazz(clazz);
                polygon.setPoints(points);
                polygon.setFill(fill);
                parent.elements.add(wrapWithJAXBElement("polygon", polygon));
                return parent;
            }

            public SvgPolygonBuilder id(String id) {
                this.id = id;
                return this;
            }

            public SvgPolygonBuilder clazz(String clazz) {
                this.clazz = clazz;
                return this;
            }

            public SvgPolygonBuilder points(String points) {
                this.points = points;
                return this;
            }

            public SvgPolygonBuilder fill(String fill) {
                this.fill = fill;
                return this;
            }
        }
    }
}
