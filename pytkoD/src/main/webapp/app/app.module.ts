import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { MicrosoftSharedModule } from 'app/shared/shared.module';
import { MicrosoftCoreModule } from 'app/core/core.module';
import { MicrosoftAppRoutingModule } from './app-routing.module';
import { MicrosoftHomeModule } from './home/home.module';
import { MicrosoftEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    MicrosoftSharedModule,
    MicrosoftCoreModule,
    MicrosoftHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    MicrosoftEntityModule,
    MicrosoftAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class MicrosoftAppModule {}
