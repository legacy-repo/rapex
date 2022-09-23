library(ggplot2)
library(plotly)
data(iris)
plot <- ggplot(data = iris, aes(x = Sepal.Length, y = Sepal.Width)) +
        geom_point(aes(shape = Species, color = Species))
plotly::ggplotly(plot)
