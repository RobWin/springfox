package springdox.documentation.spring.web.mixins

import com.fasterxml.classmate.TypeResolver
import org.springframework.plugin.core.OrderAwarePluginRegistry
import org.springframework.plugin.core.PluginRegistry
import springdox.documentation.spi.DocumentationType
import springdox.documentation.spi.service.ApiListingBuilderPlugin
import springdox.documentation.spi.service.DefaultsProviderPlugin
import springdox.documentation.spi.service.DocumentationPlugin
import springdox.documentation.spi.service.ExpandedParameterBuilderPlugin
import springdox.documentation.spi.service.OperationBuilderPlugin
import springdox.documentation.spi.service.OperationModelsProviderPlugin
import springdox.documentation.spi.service.ParameterBuilderPlugin
import springdox.documentation.spi.service.ResourceGroupingStrategy
import springdox.documentation.spring.web.plugins.DocumentationPluginsManager
import springdox.documentation.spring.web.readers.operation.OperationModelsProvider
import springdox.documentation.spring.web.readers.parameter.ExpandedParameterBuilder
import springdox.documentation.spring.web.scanners.MediaTypeReader

import static com.google.common.collect.Lists.*

@SuppressWarnings("GrMethodMayBeStatic")
class ServicePluginsSupport {

  DocumentationPluginsManager defaultWebPlugins() {
    def resolver = new TypeResolver()
    PluginRegistry<ApiListingBuilderPlugin, DocumentationType> apiListingRegistry =
            OrderAwarePluginRegistry.create(newArrayList(new MediaTypeReader(resolver)))
    PluginRegistry<DocumentationPlugin, DocumentationType> documentationPlugins =
            OrderAwarePluginRegistry.create([])
    PluginRegistry<ExpandedParameterBuilderPlugin, DocumentationType> parameterExpanderPlugin =
            OrderAwarePluginRegistry.create([new ExpandedParameterBuilder(resolver)])
    PluginRegistry<ParameterBuilderPlugin, DocumentationType>  parameterBuilderPlugins=
            OrderAwarePluginRegistry.create([])
    PluginRegistry<OperationBuilderPlugin, DocumentationType>  operationBuilderPlugins=
            OrderAwarePluginRegistry.create([])
    PluginRegistry<ResourceGroupingStrategy, DocumentationType> resourceGroupingStrategies =
            OrderAwarePluginRegistry.create([])
    PluginRegistry<OperationModelsProviderPlugin, DocumentationType> modelProviders =
            OrderAwarePluginRegistry.create([new OperationModelsProvider(resolver)])
    PluginRegistry<DefaultsProviderPlugin, DocumentationType> defaultsProviders =
            OrderAwarePluginRegistry.create([])
    new DocumentationPluginsManager(documentationPlugins, apiListingRegistry, parameterBuilderPlugins,
            parameterExpanderPlugin, operationBuilderPlugins, resourceGroupingStrategies, modelProviders, defaultsProviders)
  }

  DocumentationPluginsManager customWebPlugins(List<DocumentationPlugin> documentationPlugins = [],
      List<ResourceGroupingStrategy> groupingStrategyPlugins = [],
      List<OperationBuilderPlugin> operationPlugins = [],
      List<ParameterBuilderPlugin> paramPlugins = [],
      List<DefaultsProviderPlugin> defaultProviderPlugins = []) {
    def resolver = new TypeResolver()
    PluginRegistry<ApiListingBuilderPlugin, DocumentationType> apiListingRegistry =
            OrderAwarePluginRegistry.create(newArrayList(new MediaTypeReader(resolver)))
    PluginRegistry<DocumentationPlugin, DocumentationType> documentationPluginRegistry =
            OrderAwarePluginRegistry.create(documentationPlugins)
    PluginRegistry<ExpandedParameterBuilderPlugin, DocumentationType> parameterExpanderPlugin =
            OrderAwarePluginRegistry.create([new ExpandedParameterBuilder(resolver)])
    PluginRegistry<ParameterBuilderPlugin, DocumentationType>  parameterBuilderPlugins=
            OrderAwarePluginRegistry.create(paramPlugins)
    PluginRegistry<OperationBuilderPlugin, DocumentationType>  operationBuilderPlugins=
            OrderAwarePluginRegistry.create(operationPlugins)
    PluginRegistry<ResourceGroupingStrategy, DocumentationType> resourceGroupingStrategies =
            OrderAwarePluginRegistry.create(groupingStrategyPlugins)
    PluginRegistry<OperationModelsProviderPlugin, DocumentationType> modelProviders =
            OrderAwarePluginRegistry.create([new OperationModelsProvider(resolver)])
    PluginRegistry<DefaultsProviderPlugin, DocumentationType> defaultsProviders =
            OrderAwarePluginRegistry.create(defaultProviderPlugins)
    new DocumentationPluginsManager(documentationPluginRegistry, apiListingRegistry, parameterBuilderPlugins,
            parameterExpanderPlugin, operationBuilderPlugins, resourceGroupingStrategies, modelProviders, defaultsProviders)
  }

}