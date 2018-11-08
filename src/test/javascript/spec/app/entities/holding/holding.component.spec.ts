/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { NewLocalTestModule } from '../../../test.module';
import { HoldingComponent } from 'app/entities/holding/holding.component';
import { HoldingService } from 'app/entities/holding/holding.service';
import { Holding } from 'app/shared/model/holding.model';

describe('Component Tests', () => {
    describe('Holding Management Component', () => {
        let comp: HoldingComponent;
        let fixture: ComponentFixture<HoldingComponent>;
        let service: HoldingService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NewLocalTestModule],
                declarations: [HoldingComponent],
                providers: []
            })
                .overrideTemplate(HoldingComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(HoldingComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HoldingService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Holding(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.holdings[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
