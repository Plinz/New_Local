import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { NewLocalPurchaseModule } from './purchase/purchase.module';
import { NewLocalGradeModule } from './grade/grade.module';
import { NewLocalCategoryModule } from './category/category.module';
import { NewLocalProductTypeModule } from './product-type/product-type.module';
import { NewLocalLocationModule } from './location/location.module';
import { NewLocalStockModule } from './stock/stock.module';
import { NewLocalHoldingModule } from './holding/holding.module';
import { NewLocalCartModule } from './cart/cart.module';
import { NewLocalWarehouseModule } from './warehouse/warehouse.module';
import { NewLocalImageModule } from './image/image.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        NewLocalPurchaseModule,
        NewLocalGradeModule,
        NewLocalCategoryModule,
        NewLocalProductTypeModule,
        NewLocalLocationModule,
        NewLocalStockModule,
        NewLocalHoldingModule,
        NewLocalCartModule,
        NewLocalWarehouseModule,
        NewLocalImageModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NewLocalEntityModule {}
