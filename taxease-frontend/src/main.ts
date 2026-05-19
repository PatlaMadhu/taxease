import { platformBrowser } from '@angular/platform-browser';
import { AppModule } from './app/app-module';
import { provideCharts, withDefaultRegisterables } from 'ng2-charts';

platformBrowser().bootstrapModule(AppModule, {
  providers: [provideCharts(withDefaultRegisterables())]
})
  .catch(err => console.error(err));
