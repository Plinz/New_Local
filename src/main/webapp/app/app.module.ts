import './vendor.ts';

import { NgModule, Injector } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import { Ng2Webstorage, LocalStorageService, SessionStorageService } from 'ngx-webstorage';
import { JhiEventManager } from 'ng-jhipster';

import { AuthInterceptor } from './blocks/interceptor/auth.interceptor';
import { AuthExpiredInterceptor } from './blocks/interceptor/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './blocks/interceptor/errorhandler.interceptor';
import { NotificationInterceptor } from './blocks/interceptor/notification.interceptor';
import { NewLocalSharedModule } from './shared';
import { NewLocalCoreModule } from './core';
import { NewLocalAppRoutingModule } from './app-routing.module';
import { NewLocalHomeModule } from './home/home.module';
import { NewLocalMainSearchModule } from './mainSearch/mainSearch.module';
import { NewLocalAccountModule } from './account/account.module';
import { NewLocalEntityModule } from './entities/entity.module';
import { NewLocalStockManagementModule } from './stockManagement/stockManagement.module';
import * as moment from 'moment';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { JhiMainComponent, NavbarComponent, FooterComponent, PageRibbonComponent, ActiveMenuDirective, ErrorComponent } from './layouts';
import { NewLocalShoppingModule } from './shopping/shopping.module';
import { ReviewComponent } from './review/review.component';
import { NewLocalReviewModule } from './review/review.module';

@NgModule({
    imports: [
        BrowserModule,
        NewLocalAppRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-' }),
        NewLocalSharedModule,
        NewLocalCoreModule,
        NewLocalHomeModule,
        NewLocalMainSearchModule,
        NewLocalStockManagementModule,
        NewLocalAccountModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
        NewLocalEntityModule,
        NewLocalShoppingModule,
        NewLocalReviewModule
    ],
    declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true,
            deps: [LocalStorageService, SessionStorageService]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthExpiredInterceptor,
            multi: true,
            deps: [Injector]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ErrorHandlerInterceptor,
            multi: true,
            deps: [JhiEventManager]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: NotificationInterceptor,
            multi: true,
            deps: [Injector]
        }
    ],
    bootstrap: [JhiMainComponent]
})
export class NewLocalAppModule {
    constructor(private dpConfig: NgbDatepickerConfig) {
        this.dpConfig.minDate = { year: moment().year() - 100, month: 1, day: 1 };
    }
}
