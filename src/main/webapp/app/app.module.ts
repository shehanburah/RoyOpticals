import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { RoyOpticalsSharedModule } from 'app/shared/shared.module';
import { RoyOpticalsCoreModule } from 'app/core/core.module';
import { RoyOpticalsAppRoutingModule } from './app-routing.module';
import { RoyOpticalsHomeModule } from './home/home.module';
import { RoyOpticalsEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { JhiMainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    RoyOpticalsSharedModule,
    RoyOpticalsCoreModule,
    RoyOpticalsHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    RoyOpticalsEntityModule,
    RoyOpticalsAppRoutingModule
  ],
  declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [JhiMainComponent]
})
export class RoyOpticalsAppModule {}
