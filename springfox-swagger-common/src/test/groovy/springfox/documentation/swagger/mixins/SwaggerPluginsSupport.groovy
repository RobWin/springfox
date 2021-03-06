/*
 *
 *  Copyright 2015 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */

package springfox.documentation.swagger.mixins

import com.fasterxml.classmate.TypeResolver
import org.springframework.plugin.core.PluginRegistry
import springfox.documentation.schema.plugins.SchemaPluginsManager
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.schema.ModelBuilderPlugin
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin
import springfox.documentation.spi.service.DefaultsProviderPlugin
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager
import springfox.documentation.spring.web.readers.operation.OperationModelsProvider
import springfox.documentation.spring.web.readers.parameter.ExpandedParameterBuilder
import springfox.documentation.spring.web.readers.parameter.ParameterNameReader
import springfox.documentation.spring.web.scanners.MediaTypeReader
import springfox.documentation.swagger.readers.operation.SwaggerOperationModelsProvider
import springfox.documentation.swagger.readers.parameter.SwaggerExpandedParameterBuilder
import springfox.documentation.swagger.schema.ApiModelBuilder
import springfox.documentation.swagger.schema.ApiModelPropertyPropertyBuilder
import springfox.documentation.swagger.web.SwaggerApiListingReader
import springfox.documentation.swagger.web.ClassOrApiAnnotationResourceGrouping

import static com.google.common.collect.Lists.*
import static org.springframework.plugin.core.OrderAwarePluginRegistry.*

@SuppressWarnings("GrMethodMayBeStatic")
class SwaggerPluginsSupport {
  SchemaPluginsManager swaggerSchemaPlugins() {
    PluginRegistry<ModelPropertyBuilderPlugin, DocumentationType> propRegistry =
        create(newArrayList(new ApiModelPropertyPropertyBuilder()))

    PluginRegistry<ModelBuilderPlugin, DocumentationType> modelRegistry =
        create(newArrayList(new ApiModelBuilder(new TypeResolver())))

    new SchemaPluginsManager(propRegistry, modelRegistry)
  }

  DocumentationPluginsManager swaggerServicePlugins(List<DefaultsProviderPlugin> swaggerDefaultsPlugins) {
    def resolver = new TypeResolver()
    def plugins = new DocumentationPluginsManager()
    plugins.apiListingPlugins = create(newArrayList(new MediaTypeReader(resolver), new SwaggerApiListingReader()))
    plugins.documentationPlugins = create([])
    plugins.parameterExpanderPlugins =
        create([new ExpandedParameterBuilder(resolver), new SwaggerExpandedParameterBuilder()])
    plugins.parameterPlugins = create([new ParameterNameReader(),
                                       new ParameterNameReader()])
    plugins.operationBuilderPlugins = create([])
    plugins.resourceGroupingStrategies = create([new ClassOrApiAnnotationResourceGrouping()])
    plugins.operationModelsProviders = create([
        new OperationModelsProvider(resolver),
        new SwaggerOperationModelsProvider(resolver)])
    plugins.defaultsProviders = create(swaggerDefaultsPlugins)
    return plugins
  }
}
